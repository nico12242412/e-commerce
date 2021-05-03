package ar.com.danaide.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import ar.com.danaide.domain.enumeration.CartStatus;

/**
 * A Cart.
 */
@Entity
@Table(name = "cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status;

    @OneToMany(mappedBy = "cart")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "carts", allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public Cart totalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public Cart date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CartStatus getStatus() {
        return status;
    }

    public Cart status(CartStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Cart products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Cart addProduct(Product product) {
        this.products.add(product);
        product.setCart(this);
        return this;
    }

    public Cart removeProduct(Product product) {
        this.products.remove(product);
        product.setCart(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return id != null && id.equals(((Cart) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", totalPrice=" + getTotalPrice() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
