package com.lbp.sla.niveau.de.service.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Tickets  implements Serializable {
    public Long getIdTickets() {
        return idTickets;
    }


    public void setIdTickets(Long idTickets) {
        this.idTickets = idTickets;
    }

    public String getNameTickets() {
        return nameTickets;
    }

    public void setNameTickets(String nameTickets) {
        this.nameTickets = nameTickets;
    }

    private Long idTickets;
    private String nameTickets;

    public Tickets() {
    }
}
