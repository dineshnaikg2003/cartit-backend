package com.cartit.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;
import com.cartit.entity.Product;
import com.cartit.exception.BadRequestException;
import com.cartit.repository.ProductRepository;
import com.cartit.service.InventoryService;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

	private final ProductRepository productRepository;

	public InventoryServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public void reduceStock(Cart cart) {

		for (CartItem cartItem : cart.getItems()) {

			if (!Boolean.TRUE.equals(cartItem.getActive())) {
				continue;
			}

			Product product = cartItem.getProduct();

			if (product.getStock() < cartItem.getQuantity()) {

				throw new BadRequestException("Only " + product.getStock() + " " + product.getUnit() + " of "
						+ product.getName() + " available.");
			}

			product.setStock(product.getStock() - cartItem.getQuantity());

			productRepository.save(product);
		}
	}

	@Override
	public void restoreStock(Order order) {

		for (OrderItem item : order.getOrderItems()) {

			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new BadRequestException("Product not found."));

			product.setStock(product.getStock() + item.getQuantity());

			productRepository.save(product);
		}
	}
}