package essentials.user;

import fileio.CredentialsInput;

public final class Credentials {
    private String name;
    private String password;
    private String accountType;
    private String country;
    private int balance;

    public Credentials(final CredentialsInput credentialsInput) {
        this.name = credentialsInput.getName();
        this.password = credentialsInput.getPassword();
        this.balance = credentialsInput.getBalance();
        this.country = credentialsInput.getCountry();
        this.accountType = credentialsInput.getAccountType();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(final int balance) {
        this.balance = balance;
    }
}
