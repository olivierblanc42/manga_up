package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import manga_up.manga_up.dto.UserAddressDto;
import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.UserAddressProjection;
import manga_up.manga_up.service.UserAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private static final Logger LOGGER =  LoggerFactory.getLogger(AddressController.class);

    private final UserAddressService addressService;


    public AddressController(UserAddressService addressService) {
        this.addressService = addressService;

    }


    @Operation(summary = "All addresses with pagination")
    @ApiResponse(responseCode =  "201", description = "All addresses have been retrieved")
    @GetMapping
    public ResponseEntity<Page<UserAddressProjection>> getAllAddresses(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) @ParameterObject Pageable pageable
    ) {
        LOGGER.info("Find all addresses with pagination");
        Page<UserAddressProjection> addresses = addressService.findAllByPage(pageable);
        LOGGER.info("Found {} addresses", addresses.getTotalElements());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }



    @Operation(summary=" One address")
    @GetMapping("{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Integer id) {
        LOGGER.info("Find address by id: {}", id);
        return  ResponseEntity.ok(addressService.findById(id));
    }

    @Operation(summary = "Adding address")
    @PostMapping("/add")
    public ResponseEntity<UserAddressDto> addAddress(@RequestBody UserAddressDto userAddressDto) {
        LOGGER.info("Adding address");
        return ResponseEntity.ok(addressService.save(userAddressDto));
    }



   // @Operation(summary = "delete address by id ")
   // @DeleteMapping("/{id}")
   // public void deleteAddress(@PathVariable Integer id) {
     //   LOGGER.info("Deleting address by id");
       // addressService.deleteUserAddress(id);
   // }


    @Operation(summary = "Update UserAddress")
    @PutMapping("{id}")
    public ResponseEntity<UserAddressDto>  updateAddress(@PathVariable Integer id, @RequestBody UserAddressDto userAddressDto) {
        LOGGER.info("Updating address");
        try{
            UserAddressDto userAddress = addressService.updateUserAddress(id ,userAddressDto);
            return new ResponseEntity<>(userAddress, HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("Error updating userAddress", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
