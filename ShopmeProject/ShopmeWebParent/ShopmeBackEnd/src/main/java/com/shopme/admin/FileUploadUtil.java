package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static void saveFile(String uploadDir, String fileName, MultipartFile multiPartFile) throws IOException {
		Path uplaodPath = Paths.get(uploadDir);

		if (!Files.exists(uplaodPath)) {
			Files.createDirectories(uplaodPath);

		}
		try (InputStream inputStream = multiPartFile.getInputStream()) {
			Path filePath = uplaodPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Couldn't save the file" + fileName, ex);
		}

	}

	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException ex) {
						System.out.println("couldn't able to delete the " + file);
					}

				}
			});

		} catch (IOException ex) {
			System.out.println("couldn't list directory " + dirPath);
		}

	}
}
