package com.brokerapp.controller;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brokerapp.dto.AssetDTO;
import com.brokerapp.dto.CustomerDTO;
import com.brokerapp.dto.UserDTO;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.model.User;
import com.brokerapp.service.AssetService;
import com.brokerapp.service.CustomerService;
import com.brokerapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AdminController için birim testleri
 */
@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private AssetService assetService;

    private UserDTO userDTO;
    private CustomerDTO customerDTO;
    private AssetDTO assetDTO;
    private User user;
    private Customer customer;
    private Asset asset;

    @BeforeEach
    void setUp() {
        // Test için kullanılacak veri nesnelerini hazırla
        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setActive(true);

        customerDTO = new CustomerDTO();
        customerDTO.setUsername("testcustomer");
        customerDTO.setPassword("password");
        customerDTO.setTryBalance(1000.0);
        customerDTO.setTryUsableBalance(1000.0);
        customerDTO.setActive(true);

        assetDTO = new AssetDTO();
        assetDTO.setAssetName("XYZ");
        assetDTO.setTotalSupply(150.25);
        assetDTO.setPrice(10.5);
        assetDTO.setActive(true);

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .active(true)
                .build();

        customer = Customer.builder()
                .id(1L)
                .username("testcustomer")
                .password("encodedPassword")
                .tryBalance(1000.0)
                .tryUsableBalance(1000.0)
                .active(true)
                .build();

        asset = Asset.builder()
                .id(1L)
                .assetName("XYZ")
                .totalSupply(150.25)
                .price(10.5)
                .active(true)
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUsers_Success() throws Exception {
        User user2 = User.builder()
                .id(2L)
                .username("anotheruser")
                .password("encodedPassword")
                .active(true)
                .build();

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user, user2));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[1].username", is("anotheruser")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCustomer_Success() throws Exception {
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customer);

        mockMvc.perform(post("/api/admin/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testcustomer")))
                .andExpect(jsonPath("$.tryBalance", is(1000.0)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createAsset_Success() throws Exception {
        when(assetService.createAsset(any(AssetDTO.class))).thenReturn(asset);

        mockMvc.perform(post("/api/admin/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.assetName", is("XYZ")))
                .andExpect(jsonPath("$.totalSupply", is(150.25)))
                .andExpect(jsonPath("$.price", is(10.5)));
    }
}
