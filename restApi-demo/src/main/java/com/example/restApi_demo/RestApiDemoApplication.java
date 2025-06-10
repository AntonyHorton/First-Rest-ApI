package com.example.restApi_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class RestApiDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiDemoApplication.class, args);
	}

	class Coffee {
		private final String id;
		private String name;

		public Coffee(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public Coffee(String name) {
			this(UUID.randomUUID().toString(), name);
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@RestController
	@RequestMapping("/coffees")
	class RestApiDemoController {
		private List<Coffee> coffees = new ArrayList<>();

		public RestApiDemoController() {
			coffees.addAll(List.of(
					new Coffee("Cappuccino"),
					new Coffee("Latte"),
					new Coffee("Americano")
			));
		}

		//GET
		@GetMapping
		Iterable<Coffee> getCoffees() {
			return coffees;
		}

		//GET
		@GetMapping("/{id}")
		Optional<Coffee> getCoffee(@PathVariable String id) {
			for (Coffee c : coffees) {
				if (c.getId().equals(id)) {
					return Optional.of(c);
				}
			}
			return Optional.empty();
		}

		//POST
		@PostMapping
		Coffee postCoffee(@RequestBody Coffee coffee) {
			coffees.add(coffee);
			return coffee;
		}

		//PUT(Create a type of coffee if no one found at the list of coffees)
		@PutMapping("{id}")
		ResponseEntity <Coffee> putCoffee(@PathVariable String id,@RequestBody Coffee coffee){
			int coffeeIndex = -1;

			for (Coffee c : coffees) {
				if (c.getId().equals(id)) {
					coffeeIndex = coffees.indexOf(c);
					coffees.set(coffeeIndex, coffee);
				}
			}
			return (coffeeIndex == -1) ? new ResponseEntity<>(postCoffee(coffee),
					HttpStatus.CREATED): new ResponseEntity<>(coffee, HttpStatus.OK);
		}
		//DELETE
		@DeleteMapping("{id}")
		void deleteCoffee(@PathVariable String id) {
			coffees.removeIf(c -> c.getId().equals(id));
		}
	}
}


