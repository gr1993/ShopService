package park.shop.repository.file;

import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;

public interface FileRepository {

    File save(File file);

    FileGroup save(FileGroup fileGroup);
}
