package org.growify.bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growify.bank.dto.request.ChangePasswordRequestDTO;
import org.growify.bank.dto.request.UpdateUserRequestDTO;
import org.growify.bank.dto.response.TokenResponseDTO;
import org.growify.bank.dto.response.UserResponseDTO;
import org.growify.bank.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {

    private final UserService userService;


    @Operation(
            method = "GET",
            summary = "Get all events.",
            description = "Returns a list of all users registered in the system, only with the admin token.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{" +
                                                    "\"id\":\"string\"" +
                                                    ",\"name\":\"string\"," +
                                                    "\"email\":\"string\"}"
                                    ))),

                    @ApiResponse(responseCode = "403", description = "Forbidden.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"message\":\"User isn't authorized.\"}"
                                    )))
            })
    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            method = "GET",
            summary = "Get an event by ID",
            description = "Returns the user with the specified ID. Only users with the ADMIN role can search by user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{" +
                                                    "  \"id\": \"string\"," +
                                                    "  \"name\": \"string\"," +
                                                    "  \"email\": \"string\"" +
                                                    "}"
                                    ))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"message\":\"Could not find user with id:661eff5e4af2c96e8a7dedc92\"}"
                                    ))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"message\":\"User isn't authorized.\"}"
                                    )))
            })
    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponseDTO> getByUserId(@PathVariable String id) {
        return userService.getByUserId(id);
    }

    @Operation(
            method = "PUT",
            summary = "Update an event.",
            description = "Updates the user with the specified ID." +
                    "'name' (string), " +
                    "'email' (string), " +
                    "'password' (string)." +
                    "'confirmPassword' (string)." +
                    " Only users authenticated with their token can update.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content."),

            @ApiResponse(responseCode = "400", description = "Bad Request.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"name\": [\"Name is required.\", \"Name must be between 5 and 30 characters.\"]," +
                                            "\"email\": [\"Email is required.\", \"Invalid email format.\", \"Email must be between 10 and 30 characters.\"]," +
                                            "\"password\": [\"Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\", \"Password must be between 10 and 30 characters.\", \"Password is required.\"]," +
                                            "\"confirmPassword\": [\"Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\", \"Password must be between 10 and 30 characters.\", \"Password is required.\"]," +
                                            "\"message\": [\"Passwords do not match.\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authenticated.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            ))),

            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Could not find user with id:661eff5e4af2c96e8a7dedc92\"}"
                            ))),

            @ApiResponse(responseCode = "409", description = "Conflicts.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"E-mail not available.\"}"
                            )))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<TokenResponseDTO> updateUser(
            @PathVariable String id,
            @RequestBody @Valid UpdateUserRequestDTO registerRequestDTO,
            Authentication authentication
    ) {
        return userService.updateUser(id, registerRequestDTO, authentication);
    }

    @Operation(
            method = "DELETE",
            summary = "Delete an event by ID",
            description = "Deletes the user with the specified ID. Only users with the ADMIN role can delete the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content."),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            ))),

            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Could not find user with id:661eff5e4af2c96e8a7dedc92\"}"
                            ))),
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }


    @Operation(
            method = "POST",
            summary = "Change user password.",
            description = "Endpoint to change the password of the currently authenticated user. " +
                    "Requires the old password and the new password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content."),

            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"errors\":[" +
                                            "\"Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\", " +
                                            "\"Password must be between 10 and 30 characters.\", " +
                                            "\"Password is required.\"" +
                                            "]" +
                                            "}"))),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Unauthorized.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User isn't authorized.\"}"
                            )))
    })
    @PostMapping("/change-password")
    public ResponseEntity<Void> processChangePassword(
            @RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO,
            Authentication authentication) {
        return userService.changePassword(changePasswordRequestDTO, authentication);
    }
}
