import java.util.HashMap;import java.util.Map;
public class Bank {
    private final Map<String,Account> accounts = new HashMap<>();
    public Bank(){
        accounts.put("1001", new Account("1001","1234",500.0));
        accounts.put("1002", new Account("1002","0000",1500.0));
    }
    public Account login(String id,String pin){
        Account a=accounts.get(id);
        return (a!=null && a.checkPin(pin))?a:null;
    }
    public Account find(String id){return accounts.get(id);}
}
