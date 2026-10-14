<p align="center">
  <img src="res/icon.png" width="128" alt="ConnectEaseDesktop"><br>
  <b>ConnectEaseDesktop</b><br>
  Acesso remoto da ION Sistemas — Android, iOS e Windows
</p>

> Baseado no [RustDesk](https://github.com/rustdesk/rustdesk) © Purslane Tech Pte. Ltd. e contribuidores.
> Licenciado sob [AGPL-3.0](LICENCE). Não é afiliado à equipe do RustDesk. Veja [NOTICE](NOTICE).

## Diferenças em relação ao RustDesk

- Nome, ícones e tema branco/laranja (`#FF6A00`).
- Servidor e chave pública embutidos no build via variáveis de ambiente (nunca no código).
- Chamada de voz e visualização de câmera desativadas (`src/brand.rs`, `flutter/lib/consts.dart`).
- Build pelo GitHub Actions para Android (APK + AAB), iOS (.ipa) e Windows (.exe/.msi).

O README original do RustDesk está em [docs/README-RustDesk.md](docs/README-RustDesk.md), com instruções detalhadas de compilação.

## Configuração do servidor

| Variável | Descrição |
|---|---|
| `RENDEZVOUS_SERVER` | Host do seu `hbbs` (ex.: `acesso.suaempresa.com.br`) |
| `RS_PUB_KEY` | Conteúdo de `id_ed25519.pub` do servidor |

Build local: copie `.env.example` para `.env`, preencha e exporte antes de compilar (`set -a; . ./.env; set +a`).
Sem as variáveis, o app usa os padrões do RustDesk.

## GitHub Secrets

Settings → Secrets and variables → Actions → **New repository secret**.

| Secret | Quando | Conteúdo |
|---|---|---|
| `RENDEZVOUS_SERVER` | sempre | host do servidor |
| `RS_PUB_KEY` | sempre | chave pública do servidor |
| `ANDROID_SIGNING_KEY` | Play Store | keystore `.jks` em base64 |
| `ANDROID_ALIAS` | Play Store | alias da chave |
| `ANDROID_KEY_STORE_PASSWORD` | Play Store | senha do keystore |
| `ANDROID_KEY_PASSWORD` | Play Store | senha da chave |
| `IOS_P12_BASE64` | App Store | certificado *Apple Distribution* `.p12` em base64 |
| `IOS_P12_PASSWORD` | App Store | senha do `.p12` |
| `IOS_PROFILE_BASE64` | App Store | provisioning profile App Store em base64 |
| `APPLE_TEAM_ID` | App Store | Team ID (10 caracteres) |
| `SIGN_BASE_URL` / `SIGN_SECRET_KEY` | opcional | serviço de assinatura do Windows |

Builds de release falham (job `check-brand-secrets`) se `RENDEZVOUS_SERVER` ou `RS_PUB_KEY` estiverem vazios.

### Keystore Android (uma única vez — guarde em local seguro)

```bash
keytool -genkeypair -v -keystore connectease.jks -alias connectease \
  -keyalg RSA -keysize 4096 -validity 10000
base64 -w0 connectease.jks   # valor de ANDROID_SIGNING_KEY
```

Perder o keystore impede atualizar o app na Play Store (a menos que use *Play App Signing*, recomendado).

### Credenciais iOS (requer conta Apple Developer)

1. developer.apple.com → Identifiers → registrar `com.ion.connecteasedesktop`.
2. Certificates → *Apple Distribution* → exportar `.p12` pelo Keychain → `base64 -i cert.p12` → `IOS_P12_BASE64`.
3. Profiles → *App Store* para o bundle acima, com o nome `com.ion.connecteasedesktop` → `base64 -i perfil.mobileprovision` → `IOS_PROFILE_BASE64`.
4. Membership → Team ID → `APPLE_TEAM_ID`.

Sem esses secrets, o workflow gera um `.ipa` sem assinatura (apenas validação do build).

## Gerar os builds

- **Manual**: Actions → *Flutter Nightly Build* → *Run workflow* (publica na release `nightly`).
- **Versão**: crie uma tag `vX.Y.Z` (workflow *Flutter Tag Build*).

Artefatos: `connecteasedesktop-<versão>-*.apk`, `.aab`, `-unsigned.ipa`/`.ipa`, `-x86_64.exe`/`.msi`.

## Licença

AGPL-3.0 — ver [LICENCE](LICENCE). Quem distribuir o app (inclusive pelas lojas) deve disponibilizar o código-fonte correspondente; o link está na tela "Sobre" e deve constar na descrição da loja.
