package com.example.library.service;

import com.example.library.dto.MemberRequestDTO;
import com.example.library.dto.MemberResponseDTO;
import com.example.library.mapper.MemberMapper;
import com.example.library.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO(team - assignee: ____):
 * Implement the Member service. Use AuthorService as a reference.
 *
 * Methods needed:
 *  - findAll(Pageable)              -> GET /api/members
 *  - findById(Long id)              -> GET /api/members/{id}
 *  - searchByName(String name)      -> GET /api/members/search?name=...
 *  - create(MemberRequestDTO)       -> POST /api/members   (check duplicate email -> 409)
 *  - update(Long id, MemberRequestDTO)
 *  - delete(Long id)
 *
 * membershipDate is auto-set by the entity's @PrePersist — do NOT accept it from the request DTO.
 */
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Transactional(readOnly = true)
    public Page<MemberResponseDTO> findAll(Pageable pageable) {
        // TODO(team)
        throw new UnsupportedOperationException("findAll not implemented yet");
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO findById(Long id) {
        // TODO(team)
        throw new UnsupportedOperationException("findById not implemented yet");
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> searchByName(String name) {
        // TODO(team): use memberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
        throw new UnsupportedOperationException("searchByName not implemented yet");
    }

    public MemberResponseDTO create(MemberRequestDTO request) {
        // TODO(team):
        //   1. memberRepository.findByEmail(request.getEmail()).isPresent() -> throw DuplicateResourceException
        //   2. map -> save -> return DTO
        throw new UnsupportedOperationException("create not implemented yet");
    }

    public MemberResponseDTO update(Long id, MemberRequestDTO request) {
        // TODO(team)
        throw new UnsupportedOperationException("update not implemented yet");
    }

    public void delete(Long id) {
        // TODO(team)
        throw new UnsupportedOperationException("delete not implemented yet");
    }
}
