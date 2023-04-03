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
}
