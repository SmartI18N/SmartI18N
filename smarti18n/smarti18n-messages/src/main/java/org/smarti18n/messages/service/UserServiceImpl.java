package org.smarti18n.messages.service;

import org.smarti18n.api.User;
import org.smarti18n.messages.entities.GitHubUser;
import org.smarti18n.messages.repositories.GitHubUserRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class UserServiceImpl implements UserService {

    private final GitHubUserRepository gitHubUserRepository;

    public UserServiceImpl(final GitHubUserRepository gitHubUserRepository) {
        this.gitHubUserRepository = gitHubUserRepository;
    }

    @Override
    @Transactional
    public void logGitHubLogin(final User user) {
        final GitHubUser gitHubUser = this.gitHubUserRepository.findById(user.getId())
                .orElseGet(() -> new GitHubUser(user.getId()));

        gitHubUser.setLogin(user.getLogin());
        gitHubUser.setEmail(user.getEmail());
        gitHubUser.setName(user.getName());
        gitHubUser.setType(user.getType());
        gitHubUser.setCompany(user.getCompany());
        gitHubUser.addLogin();

        this.gitHubUserRepository.save(gitHubUser);
    }
}
