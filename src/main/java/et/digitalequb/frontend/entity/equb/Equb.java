
package et.digitalequb.frontend.entity.equb;

import et.digitalequb.frontend.entity.equbtegna.Equbtegna;
import et.digitalequb.frontend.entity.payment.Payments;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Equb {
    private Integer id;
    private String name;
    private int amount;
    private int round;
    private String started_date;
    private String end_date;
    private String status;
    private List<Equbtegna> equbtegnas;
    private List<StartEqub> startEqubs;
    private EqubAgreement equbAgreement;
    private List<Payments> payments;

    private String equbType;
    private String equbCategory;
    private Date createdAt;
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Payments> getPayments() {
        return payments;
    }

    public void setPayments(List<Payments> payments) {
        this.payments = payments;
    }

    public String getStarted_date() {
        return started_date;
    }

    public void setStarted_date(String started_date) {
        this.started_date = started_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<Equbtegna> getEqubtegnas() {
        return equbtegnas;
    }

    public void setEqubtegnas(List<Equbtegna> equbtegnas) {
        this.equbtegnas = equbtegnas;
    }

    public List<StartEqub> getStartEqubs() {
        return startEqubs;
    }

    public void setStartEqubs(List<StartEqub> startEqubs) {
        this.startEqubs = startEqubs;
    }

    public EqubAgreement getEqubAgreement() {
        return equbAgreement;
    }

    public void setEqubAgreement(EqubAgreement equbAgreement) {
        this.equbAgreement = equbAgreement;
    }

  /*  public EqubType getEqubType() {
        return equbType;
    }

    public void setEqubType(EqubType equbType) {
        this.equbType = equbType;
    }

    public EqubCategory getEqubCategory() {
        return equbCategory;
    }

    public void setEqubCategory(EqubCategory equbCategory) {
        this.equbCategory = equbCategory;
    }*/

    public String getEqubType() {
        return equbType;
    }

    public void setEqubType(String equbType) {
        this.equbType = equbType;
    }

    public String getEqubCategory() {
        return equbCategory;
    }

    public void setEqubCategory(String equbCategory) {
        this.equbCategory = equbCategory;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}