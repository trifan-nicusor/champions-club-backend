package ro.championsclub.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
class EquipmentControllerTest {
/*

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    @MockBean
    JwtService jwtService;

    @MockBean
    private AuthService authService;

    @MockBean
    ConstraintValidator constraintValidator;

    private static final String PATH = "/api/v1/equipment";

    private MockMultipartFile imageFile;
    private EquipmentUpdateRequest updateRequest;
    private EquipmentAdminView updatedEquipment;

    @BeforeEach
    public void setup() {
        // Initialize a mock image file
        imageFile = new MockMultipartFile(
                "image",
                "updated-equipment-image.png",
                MediaType.IMAGE_PNG_VALUE,
                "Updated Mock Image Content".getBytes()
        );

        // Initialize a mock EquipmentUpdateRequest object
        updateRequest = new EquipmentUpdateRequest();
        updateRequest.setName("Updated Excavator");
        updateRequest.setCategory(EquipmentCategoryEnum.CARDIO);

        // Initialize a mock EquipmentAdminView (response object)
        updatedEquipment = new EquipmentAdminView();
        updatedEquipment.setId(1);
        updatedEquipment.setName("Updated Excavator");
        updateRequest.setCategory(EquipmentCategoryEnum.CARDIO);
    }

    @Test
    void saveEquipmentTest() throws Exception {
        var request = new EquipmentRequest();
        request.setName("name");
        request.setCategory("FUNCTIONAL");

        String requestJson = objectMapper.writeValueAsString(request);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3, 4}
        );

        MockMultipartFile requestPart = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                        .file(image)
                        .file(requestPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    public void disableEquipmentTest() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateEquipmentTest() throws Exception {
        */
/*
        * var request = new EquipmentRequest();
        request.setName("name");
        request.setCategory("FUNCTIONAL");

        String requestJson = objectMapper.writeValueAsString(request);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3, 4}
        );

        MockMultipartFile requestPart = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
        );
        * *//*


        // Mock the service behavior for a successful update
        Mockito.when(equipmentService.updateEquipment(Mockito.anyInt(), Mockito.any(), Mockito.any(EquipmentUpdateRequest.class)))
                .thenReturn(updatedEquipment);

        // Convert EquipmentUpdateRequest to JSON
        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.multipart(PATH + "/{id}", 1)
                        .file(imageFile)
                        .file(new MockMultipartFile(
                                "request",
                                "",
                                MediaType.APPLICATION_JSON_VALUE,
                                requestJson.getBytes()
                        ))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expecting HTTP 200 OK
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Excavator"))
                .andExpect(jsonPath("$.category").value("Heavy Equipment"));
    }
*/

}