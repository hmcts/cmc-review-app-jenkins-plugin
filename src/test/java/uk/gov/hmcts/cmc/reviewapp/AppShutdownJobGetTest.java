package uk.gov.hmcts.cmc.reviewapp;


import hudson.model.Job;
import jenkins.model.Jenkins;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppShutdownJobGetTest {

    @Mock
    private Jenkins jenkins;
    @Mock
    private Job job;

    @Test
    public void shouldReturnTheJobIfItsFound() {
        when(jenkins.getItemByFullName(anyString())).thenReturn(job);
        assertThat(AppShutdownJob.get(jenkins)).isEqualTo(job);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateWhenJobIsNotFound() {
        when(jenkins.getItemByFullName(anyString())).thenReturn(null);
        AppShutdownJob.get(jenkins);
    }

}
