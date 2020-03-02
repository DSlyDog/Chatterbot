package net.whispwriting.mystery_dungeon;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class SetToken {

    public JDA setup() throws LoginException {
        return new JDABuilder(AccountType.BOT).setToken("NjQxNzUzMTUyNjMwMjkyNTQx.XcM96A.huvVtP7w9MyWQjeUbIqee0g3-Oo").build();
    }

}
