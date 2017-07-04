package com.banana.spring.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.banana.config.StorageConfig;

@RunWith(SpringRunner.class)
public class FileSystemStorageServiceTests {
  @MockBean
  private StorageConfig properties;

  private StorageService fileSystemStorageService;

  @Before
  public void setUp() {
    given(this.properties.getLocation()).willReturn("/");

    this.fileSystemStorageService = new FileSystemStorageService(properties);
  }

  @Test
  public void shouldGenerateNewFilenameWithRandomHash() {
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());

    String newName = this.fileSystemStorageService.generateNewFilename(multipartFile);
    assertThat(newName).startsWith("test_");
    assertThat(newName).endsWith(".txt");
  }
}
