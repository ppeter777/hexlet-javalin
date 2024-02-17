package org.example.hexlet.dto.users;

import org.example.hexlet.model.User;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

// BEGIN
@AllArgsConstructor
@Getter
public class UsersPage {
    private List<User> users;
}
