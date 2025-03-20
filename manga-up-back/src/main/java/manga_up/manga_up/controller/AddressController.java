package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import manga_up.manga_up.model.Address;
import manga_up.manga_up.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private static final Logger LOGGER= LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @Operation(summary = "All addresses with pagination")
    @ApiResponse(responseCode =  "201", description = "All addresses have been retrieved")
    @GetMapping
    public ResponseEntity<Page<Address>> getAllAddresses(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all addresses with pagination");
        Page<Address> addresses = addressService.findAllByPage(pageable);
        LOGGER.info("Found {} addresses", addresses.getTotalElements());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

}
