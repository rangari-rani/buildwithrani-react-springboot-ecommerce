package com.buildwithrani.backend.integration.audit;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import com.buildwithrani.backend.audit.repository.AuditLogRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditLogRepository auditLogRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnLogsByEntityTypeAndEntityId() throws Exception {

        when(auditLogRepository
                .findByEntityTypeAndEntityIdOrderByTimestampDesc("ORDER", 1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/audit-logs")
                        .param("entityType", "ORDER")
                        .param("entityId", "1"))
                .andExpect(status().isOk());

        verify(auditLogRepository)
                .findByEntityTypeAndEntityIdOrderByTimestampDesc("ORDER", 1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnLogsByEntityType() throws Exception {

        when(auditLogRepository
                .findByEntityTypeOrderByTimestampDesc("ORDER"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/audit-logs")
                        .param("entityType", "ORDER"))
                .andExpect(status().isOk());

        verify(auditLogRepository)
                .findByEntityTypeOrderByTimestampDesc("ORDER");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllAuditLogs() throws Exception {

        when(auditLogRepository.findAllByOrderByTimestampDesc())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/audit-logs"))
                .andExpect(status().isOk());

        verify(auditLogRepository)
                .findAllByOrderByTimestampDesc();
    }
}