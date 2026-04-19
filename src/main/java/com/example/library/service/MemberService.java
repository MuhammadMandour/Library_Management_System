package com.example.library.service;

import com.example.library.dto.MemberRequestDTO;
import com.example.library.dto.MemberResponseDTO;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.entity.Member;
import com.example.library.mapper.MemberMapper;
import com.example.library.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return memberRepository.findAll(pageable)
                .map(memberMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
        return memberMapper.toResponseDTO(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> searchByName(String name) {
        return memberMapper.toResponseDTOList(
                memberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
        );
    }

    public MemberResponseDTO create(MemberRequestDTO request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already in use");
        }
        Member member = memberMapper.toEntity(request);
        Member saved = memberRepository.save(member);
        return memberMapper.toResponseDTO(saved);
    }

    public MemberResponseDTO update(Long id, MemberRequestDTO request) {
        Member existing = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
        memberMapper.updateEntityFromDTO(request, existing);
        Member saved = memberRepository.save(existing);
        return memberMapper.toResponseDTO(saved);
    }

    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member", id);
        }
        memberRepository.deleteById(id);
    }
}