package ar.com.danaide.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@DiscriminatorValue("PROMOTED")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PromotedCart extends Cart implements Serializable {

	private static final long serialVersionUID = 1L;

	private Float discount;

	private Float price;

	private Float fixedPrice;

	public Float setDiscount(Float fixedPrice) {
		return this.discount = fixedPrice;
	}

	public Float getPrice() {
		return price - discount;
	}

}
