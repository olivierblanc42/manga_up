package manga_up.manga_up.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import manga_up.manga_up.dto.UserAdress.UserAddressDto;
import manga_up.manga_up.model.UserAddress;
import manga_up.manga_up.projection.userAdress.UserAddressProjection;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private static final Logger LOGGER =  LoggerFactory.getLogger(AddressController.class);

    private final UserAddressService addressService;


    public AddressController(UserAddressService addressService) {
        this.addressService = addressService;

    }

   @PreAuthorize("hasRole('ADMIN')")  
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


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary=" One address")
    @GetMapping("{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Integer id) {
        LOGGER.info("Find address by id: {}", id);
        return  ResponseEntity.ok(addressService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Adding address")
    @PostMapping("/add")
    public ResponseEntity<UserAddressDto> addAddress(@RequestBody UserAddressDto userAddressDto) {
        LOGGER.info("Adding address");
        return ResponseEntity.ok(addressService.save(userAddressDto));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete address by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted"),
            @ApiResponse(responseCode = "400", description = "Address used by users"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Integer id) {
       LOGGER.info("Deleting address by id");
        addressService.deleteUserAddress(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
