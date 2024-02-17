@import org.example.hexlet.dto.users.UsersPage
@param UsersPage page

<html>
    <head>
        <title>Users page</title>
    </head>
    <body>
        <table>
            <th>Name</th>
            <th>Email</th>
            <th>Password</th>
            @for (var user : page.getUsers())
                <tr>
                    <td>${user.getName()}</td>
                    <td>${user.getEmail()}</td>
                    <td>${user.getPassword()}</td>
                </tr>
            @endfor
        </table>
    </body>
</html>