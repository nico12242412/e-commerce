package ar.com.danaide.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.com.danaide.domain.Cart;
import ar.com.danaide.domain.Product;
import ar.com.danaide.domain.PromotedCart;
import ar.com.danaide.repository.CartRepository;
import ar.com.danaide.repository.ProductRepository;
import ar.com.danaide.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.com.danaide.domain.Cart}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CartResource {

	private final Logger log = LoggerFactory.getLogger(CartResource.class);

	private static final String ENTITY_NAME = "cart";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final CartRepository cartRepository;

	private final ProductRepository productRepository;

	public CartResource(CartRepository cartRepository, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
	}

	/**
	 * {@code POST  /carts} : Create a new cart.
	 *
	 * @param cart the cart to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new cart, or with status {@code 400 (Bad Request)} if the
	 *         cart has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/carts")
	public ResponseEntity<Cart> createCart(@RequestBody Cart cart) throws URISyntaxException {
		log.debug("REST request to save Cart : {}", cart);
		if (cart.getId() != null) {
			throw new BadRequestAlertException("A new cart cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Cart result = cartRepository.save(cart);
		return ResponseEntity
				.created(new URI("/api/carts/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /carts} : Updates an existing cart.
	 *
	 * @param cart the cart to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated cart, or with status {@code 400 (Bad Request)} if the
	 *         cart is not valid, or with status {@code 500 (Internal Server Error)}
	 *         if the cart couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/carts")
	public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) throws URISyntaxException {
		log.debug("REST request to update Cart : {}", cart);
		if (cart.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Cart result = cartRepository.save(cart);
		return ResponseEntity.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cart.getId().toString()))
				.body(result);
	}

	/**
	 * {@code GET  /carts} : get all the carts.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of carts in body.
	 */
	@GetMapping("/carts")
	public List<Cart> getAllCarts() {
		log.debug("REST request to get all Carts");
		return cartRepository.findAll();
	}

	/**
	 * {@code GET  /carts/:id} : get the "id" cart.
	 *
	 * @param id the id of the cart to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the cart, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/carts/{id}")
	public ResponseEntity<Cart> getCart(@PathVariable Long id) {
		log.debug("REST request to get Cart : {}", id);
		Optional<Cart> cart = cartRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(cart);
	}

	/**
	 * {@code DELETE  /carts/:id} : delete the "id" cart.
	 *
	 * @param id the id of the cart to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/carts/{id}")
	public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
		log.debug("REST request to delete Cart : {}", id);
		cartRepository.deleteById(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}

	@PutMapping("/carts/{cart_id}")
	public ResponseEntity<Cart> addProduct(@PathVariable(name = "cart_id", required = true) Long cartId,
			@RequestBody(required = true) Product product) {

		Cart cart = cartRepository.findById(cartId).get();

		if (cart == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}

		try {
			product.cart(cart);
			productRepository.save(product);
			cart.addProduct(product);
			cart.setTotalPrice(getCartTotalPrice(cart));
			cartRepository.save(cart);

		} catch (Exception e) {
			throw new BadRequestAlertException("Invalid entity", " product ", product.toString());
		}
		ResponseEntity<Cart> response = new ResponseEntity<Cart>(cart, HttpStatus.ACCEPTED);
		return response;
	}

	@DeleteMapping("/carts/{cart_id}")
	public ResponseEntity<Cart> deleteProduct(@PathVariable(name = "cart_id", required = true) Long cartId,
			@RequestBody(required = true) Product product) {

		Cart cart = cartRepository.findById(cartId).get();

		if (cart == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}

		try {
			cart.removeProduct(product);
		} catch (Exception e) {
			throw new BadRequestAlertException("Invalid entity", " product ", product.toString());
		}
		ResponseEntity<Cart> response = new ResponseEntity<Cart>(cart, HttpStatus.ACCEPTED);
		return response;
	}

	private Float getCartTotalPrice(Cart cart) {
		Float productsPrice = cart.getProducts().stream().map(prod -> prod.getPrice()).reduce(0f, Float::sum);
		Float totalPrice = cart.getTotalPrice();

		if (cart.getProducts().size() > 5 && cart.getProducts().size() < 10) {
			totalPrice = productsPrice * 0.95f;
		} else if (cart.getProducts().size() > 10) {

			// Si el carro es promocionable
			if (PromotedCart.class.isAssignableFrom(cart.getClass())) {
				// calculo las fechas del [ultimo mes
				LocalDate date = cart.getDate();
				date = date.minusMonths(1);
				LocalDate from = date.withDayOfMonth(1);
				LocalDate to = date.withDayOfMonth(date.lengthOfMonth());

				// Consulto por los carritos del customer del mes pasado
				List<Cart> customerCarts = cartRepository.findAllByCustomerAndDateStartAndDateEnd(cart.getCustomer(),
						from, to);

				Float lastMonthTotalPrice = customerCarts.stream().map(c -> c.getTotalPrice()).reduce(0f, Float::sum);
				if (lastMonthTotalPrice > 500f)
					totalPrice = 500f;
				else
					totalPrice = lastMonthTotalPrice * .95f;
			} else {
				totalPrice = productsPrice * 0.90f;
			}
		}

		return totalPrice;

	}
}
