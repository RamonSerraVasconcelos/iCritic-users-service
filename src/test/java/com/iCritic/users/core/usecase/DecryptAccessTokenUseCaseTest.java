package com.iCritic.users.core.usecase;

import com.iCritic.users.core.fixture.AccessTokenFixture;
import com.iCritic.users.core.model.AccessToken;
import com.iCritic.users.core.usecase.boundary.GenerateDecryptedTokenBoundary;
import com.iCritic.users.entrypoint.fixture.JwtTokenFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecryptAccessTokenUseCaseTest {

    @InjectMocks
    private DecryptAccessTokenUseCase decryptAccessTokenUseCase;

    @Mock
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Mock
    private GenerateDecryptedTokenBoundary generateDecryptedTokenBoundary;

    @Test
    void givenToken_ThenCallTokenValidation_AndReturnDecryptedToken() {
        String token = JwtTokenFixture.load();
        String decryptedTokenFixture = JwtTokenFixture.load();
        AccessToken accessToken = AccessTokenFixture.load();

        when(validateAccessTokenUseCase.execute(token)).thenReturn(accessToken);
        when(generateDecryptedTokenBoundary.execute(any(), any(), any(), any(), any())).thenReturn(decryptedTokenFixture);

        String decryptedToken = decryptAccessTokenUseCase.execute(token);

        verify(validateAccessTokenUseCase).execute(token);
        verify(generateDecryptedTokenBoundary).execute(any(), any(), any(), any(), any());

        assertThat(decryptedToken).isNotEmpty().isEqualTo(decryptedTokenFixture);
    }
}