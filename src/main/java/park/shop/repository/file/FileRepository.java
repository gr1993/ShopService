package park.shop.repository.file;

import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;

import java.util.Optional;

public interface FileRepository {

    File save(File file);

    FileGroup save(FileGroup fileGroup);

    Optional<File> findById(Long id);
}
