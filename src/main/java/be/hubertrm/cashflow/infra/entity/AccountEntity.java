package be.hubertrm.cashflow.infra.entity;

import be.hubertrm.cashflow.domain.model.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creation_date", nullable = false)
    private LocalDate date;

    public static AccountEntity from(Account account) {
        return new AccountEntity(
                account.getId(),
                account.getName(),
                account.getDate()
        );
    }

    public Account fromThis() {
        return new Account(id, name, date);
    }
}
