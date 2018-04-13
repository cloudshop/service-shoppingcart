package com.eyun.shoppingcart.service;

import org.springframework.web.bind.annotation.GetMapping;

import com.eyun.shoppingcart.client.AuthorizedFeignClient;
import com.eyun.shoppingcart.service.dto.UserDTO;

@AuthorizedFeignClient(name="uaa")
public interface UaaService {

	@GetMapping("/api/account")
	public UserDTO getAccount();
	
}
