package com.imooc.handlers;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.imooc.domain.User;
import com.imooc.repository.UserRepository;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

	private final UserRepository repository;

	public UserHandler(UserRepository rep) {
		this.repository = rep;
	}

	/**
	 * 得到所有用户
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> getAllUser(ServerRequest request) {
		return ok().contentType(APPLICATION_JSON_UTF8)
				.body(this.repository.findAll(), User.class);
	}

	/**
	 * 创建用户
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> createUser(ServerRequest request) {
		Mono<User> user = request.bodyToMono(User.class);
		return user.flatMap(u -> {
			return ok().contentType(APPLICATION_JSON_UTF8)
					.body(this.repository.saveAll(user), User.class);
		});
	}

	/**
	 * 根据id删除用户
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> deleteUserById(ServerRequest request) {
		String id = request.pathVariable("id");

		return this.repository.findById(id)
				.flatMap(
						user -> this.repository.delete(user).then(ok().build()))
				.switchIfEmpty(notFound().build());
	}

}
