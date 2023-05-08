package uz.tuit.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.domain.QueueForDoctor;
import uz.tuit.repository.QueueForDoctorRepository;
import uz.tuit.service.dto.QueueForDoctorDTO;
import uz.tuit.service.mapper.QueueForDoctorMapper;

/**
 * Service Implementation for managing {@link QueueForDoctor}.
 */
@Service
@Transactional
public class QueueForDoctorService {

    private final Logger log = LoggerFactory.getLogger(QueueForDoctorService.class);

    private final QueueForDoctorRepository queueForDoctorRepository;

    private final QueueForDoctorMapper queueForDoctorMapper;

    public QueueForDoctorService(QueueForDoctorRepository queueForDoctorRepository, QueueForDoctorMapper queueForDoctorMapper) {
        this.queueForDoctorRepository = queueForDoctorRepository;
        this.queueForDoctorMapper = queueForDoctorMapper;
    }

    /**
     * Save a queueForDoctor.
     *
     * @param queueForDoctorDTO the entity to save.
     * @return the persisted entity.
     */
    public QueueForDoctorDTO save(QueueForDoctorDTO queueForDoctorDTO) {
        log.debug("Request to save QueueForDoctor : {}", queueForDoctorDTO);
        QueueForDoctor queueForDoctor = queueForDoctorMapper.toEntity(queueForDoctorDTO);
        queueForDoctor = queueForDoctorRepository.save(queueForDoctor);
        return queueForDoctorMapper.toDto(queueForDoctor);
    }

    /**
     * Update a queueForDoctor.
     *
     * @param queueForDoctorDTO the entity to save.
     * @return the persisted entity.
     */
    public QueueForDoctorDTO update(QueueForDoctorDTO queueForDoctorDTO) {
        log.debug("Request to update QueueForDoctor : {}", queueForDoctorDTO);
        QueueForDoctor queueForDoctor = queueForDoctorMapper.toEntity(queueForDoctorDTO);
        queueForDoctor = queueForDoctorRepository.save(queueForDoctor);
        return queueForDoctorMapper.toDto(queueForDoctor);
    }

    /**
     * Partially update a queueForDoctor.
     *
     * @param queueForDoctorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QueueForDoctorDTO> partialUpdate(QueueForDoctorDTO queueForDoctorDTO) {
        log.debug("Request to partially update QueueForDoctor : {}", queueForDoctorDTO);

        return queueForDoctorRepository
            .findById(queueForDoctorDTO.getId())
            .map(existingQueueForDoctor -> {
                queueForDoctorMapper.partialUpdate(existingQueueForDoctor, queueForDoctorDTO);

                return existingQueueForDoctor;
            })
            .map(queueForDoctorRepository::save)
            .map(queueForDoctorMapper::toDto);
    }

    /**
     * Get all the queueForDoctors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QueueForDoctorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QueueForDoctors");
        return queueForDoctorRepository.findAll(pageable).map(queueForDoctorMapper::toDto);
    }

    /**
     * Get all the queueForDoctors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<QueueForDoctorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return queueForDoctorRepository.findAllWithEagerRelationships(pageable).map(queueForDoctorMapper::toDto);
    }

    /**
     * Get one queueForDoctor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QueueForDoctorDTO> findOne(Long id) {
        log.debug("Request to get QueueForDoctor : {}", id);
        return queueForDoctorRepository.findOneWithEagerRelationships(id).map(queueForDoctorMapper::toDto);
    }

    /**
     * Delete the queueForDoctor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QueueForDoctor : {}", id);
        queueForDoctorRepository.deleteById(id);
    }
}
