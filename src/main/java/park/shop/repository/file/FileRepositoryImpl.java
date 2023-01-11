package park.shop.repository.file;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class FileRepositoryImpl implements FileRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public FileRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public File save(File file) {
        em.persist(file);
        return file;
    }

    @Override
    public FileGroup save(FileGroup fileGroup) {
        em.persist(fileGroup);
        return fileGroup;
    }
}
