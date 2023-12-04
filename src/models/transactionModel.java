import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class transactionModel {
    private int id;
    private String type;
    private int accountId;
    private double amount;
    private String date;
}
