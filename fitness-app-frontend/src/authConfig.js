export const authConfig = {
    clientId: 'fitness-oauth2',
    authorizationEndpoint: 'http://127.0.0.1:8181/realms/fitnes_OAuth2/protocol/openid-connect/auth',
    tokenEndpoint: 'http://127.0.0.1:8181/realms/fitnes_OAuth2/protocol/openid-connect/token',
    redirectUri: 'http://localhost:5173/',
    scope: 'openid profile email offline_access',
    onRefreshTokenExpire: (event) => event.logIn()
    }