package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    /**
     * no-args constructor
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * TO-DO: Use AccountDAO to add an Account
     */
    public Account addAccount(Account account) {
        Account newAccount = accountDAO.insertAccount(account);
        // System.out.println(newAccount.toString());
        return newAccount;
    }

    /**
     * TO-DO: Use AccountDAO to validate an Account
     */
    public Account validateAccount(Account account) {
        return accountDAO.validateAccount(account);
    }
}
