package com.example.daydreamer.service;

import com.example.daydreamer.entity.Account;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.enums.AccountRole;
import com.example.daydreamer.model.account.AccountRequest;
import com.example.daydreamer.model.account.AccountResponse;
import com.example.daydreamer.repository.AccountRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.specification.GenericSpecification;
import com.example.daydreamer.utils.CloudinaryUtils;
import com.example.daydreamer.utils.CustomValidationException;
import com.example.daydreamer.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final StudioRepository studioRepository;
    private final CloudinaryUtils cloudinaryUtils;

    public List<AccountResponse> searchAccounts(String studioId,
                                                String username,
                                                String fullName,
                                                String address,
                                                String dob,
                                                String gender,
                                                String phoneNumber,
                                                String nationality,
                                                AccountRole role,
                                                String status,
                                                int page,
                                                int limit) {
        LOGGER.info("Searching accounts with dynamic criteria");

        Specification<Account> spec = buildSpecification(studioId, username, fullName, address, dob, gender, phoneNumber, nationality, role, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Account> accountPage = accountRepository.findAll(spec, pageable);

        return accountPage.stream()
            .map(this::accountResponseGenerator)
            .collect(Collectors.toList());
    }

    private Specification<Account> buildSpecification(String studioId,
                                                      String username,
                                                      String fullName,
                                                      String address,
                                                      String dob,
                                                      String gender,
                                                      String phoneNumber,
                                                      String nationality,
                                                      AccountRole role,
                                                      String status) {
        Specification<Account> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, username, "username", "like");
        spec = GenericSpecification.addSpecification(spec, fullName, "fullName", "like");
        spec = GenericSpecification.addSpecification(spec, address, "address", "like");
        spec = GenericSpecification.addSpecification(spec, dob, "dob", "equal");
        spec = GenericSpecification.addSpecification(spec, gender, "gender", "equal");
        spec = GenericSpecification.addSpecification(spec, phoneNumber, "phoneNumber", "like");
        spec = GenericSpecification.addSpecification(spec, nationality, "nationality", "equal");
        spec = GenericSpecification.addSpecification(spec, role, "auth.role", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<AccountResponse> findAll(int page, int limit) {
        LOGGER.info("Find all accounts");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        if (accountPage.isEmpty()) {
            LOGGER.warn("No accounts were found!");
        }

        return accountPage.stream()
                .map(this::accountResponseGenerator)
                .collect(Collectors.toList());
    }

    public AccountResponse findById(String id) {
        LOGGER.info("Find account with id " + id);
        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            LOGGER.warn("No account was found!");
            return null;
        }
        return account.map(this::accountResponseGenerator).get();
    }

    public AccountResponse uploadAvatar(String accountId, MultipartFile avatar) throws IOException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomValidationException(List.of("No account was found!")));

        account.setAvatarLink(cloudinaryUtils.uploadImage(avatar));
        accountRepository.save(account);
        return accountResponseGenerator(account);
    }

    public AccountResponse save(AccountRequest accountRequest) {
        Account account;
        Studio studio;
        if (accountRequest.getStudioId() != null) {
            studio = studioRepository.findById(accountRequest.getStudioId())
                    .orElseThrow(() -> new CustomValidationException(List.of("No studio was found!")));
        } else {
            studio = null;
        }

        if (accountRequest.getId() != null) {
            LOGGER.info("Update account with id " + accountRequest.getId());
            checkExist(accountRequest.getId());
            account = accountRepository.findById(accountRequest.getId()).get();
        } else {
            LOGGER.info("Create new account");
            account = new Account();
        }

        account.setStudio(studio);
        account.setUsername(accountRequest.getUsername());
        account.setFullName(accountRequest.getFullName());
        account.setAddress(accountRequest.getAddress());
        account.setDob(accountRequest.getDob());
        account.setGender(accountRequest.getGender());
        account.setPhoneNumber(accountRequest.getPhoneNumber());
        account.setNationality(accountRequest.getNationality());
        account.setInstagram(accountRequest.getInstagram());
        account.setStatus(accountRequest.getStatus());

        accountRepository.save(account);

        return accountResponseGenerator(account);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete account with id " + id);
            checkExist(id);
            Account account = accountRepository.findById(id).get();
            accountRepository.delete(account);
        }
    }

    public AccountResponse accountResponseGenerator(Account account) {
        return ResponseUtil.generateResponse(account, AccountResponse.class);
    }

    private void checkExist(String id) {
        if (accountRepository.findById(id).isEmpty()) {
            LOGGER.error("No account was found!");
            throw new CustomValidationException(List.of("No account was found!"));
        }
    }
}
