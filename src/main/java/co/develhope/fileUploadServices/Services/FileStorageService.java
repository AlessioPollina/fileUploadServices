package co.develhope.fileUploadServices.Services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    Environment environment;

    /**
     *
     * @param file File from upload Controller
     * @return New file name with extension
     * @throws IOException if folder is not writable
     */
    public String upload(MultipartFile file)throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName + "." + extension;
        //Verification on Repository Folder
        File fileFolder = new File(environment.getProperty("fileRepositoryFolder"));
        if (!fileFolder.exists()) throw new IOException("Final folder dies not exist");
        if (!fileFolder.isDirectory()) throw new IOException("Final folder is not a directory");
        File fileDestination = new File(environment.getProperty("fileRepositoryFolder") + "\\" + completeFileName);
        if (fileDestination.exists()) throw new IOException ("File Conflict");
        file.transferTo(fileDestination);
        return completeFileName;
    };

    public byte[] download(String fileName)throws IOException{
        File fileFromRepository = new File(environment.getProperty("fileRepositoryFolder") + "\\" + fileName);
        if (!fileFromRepository.exists()) throw new IOException ("File does not exist");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));

    };
}
