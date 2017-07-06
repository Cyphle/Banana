package com.banana.view.controllers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.banana.BananaApplication;
import com.banana.config.WebSecurityConfig;
import com.banana.view.storage.StorageFileNotFoundException;
import com.banana.view.storage.StorageService;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {BananaApplication.class, WebSecurityConfig.class})
@TestPropertySource(locations = {"classpath:application-test.properties"})
public class FileUploadControllerTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private StorageService storageService;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private Filter springSecurityFilterChain;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilters(springSecurityFilterChain)
            .build();
  }

  @Test
  public void should_list_all_files() throws Exception {
    given(this.storageService.loadAll())
            .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

    this.mvc.perform(get("/storage"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("files",
                    Matchers.contains("http://localhost/storage/files/first.txt", "http://localhost/storage/files/second.txt")));
  }

  @Test
  public void should_save_uploaded_file() throws Exception {
    MockMultipartFile multipartFile =
            new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
    this.mvc.perform(fileUpload("/storage").file(multipartFile).with(csrf()))
            .andExpect(status().isFound())
            .andExpect(header().string("Location", "/storage"));

    then(this.storageService).should().store(multipartFile);
  }

  @Test
  public void should_404_when_missing_file() throws Exception {
    given(this.storageService.loadAsResource("test.txt"))
            .willThrow(StorageFileNotFoundException.class);

    this.mvc.perform(get("/storage/files/test.txt"))
            .andExpect(status().isNotFound());
  }
}
