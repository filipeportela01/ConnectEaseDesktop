use hbb_common::config;

pub const APP_NAME: &str = "ConnectEaseDesktop";

// Injected at compile time (CI: GitHub Secrets) so the server and key never live in the source.
// Empty or unset falls back to the upstream defaults.
fn build_env(value: Option<&'static str>) -> Option<&'static str> {
    value.map(str::trim).filter(|v| !v.is_empty())
}

pub fn rendezvous_server() -> Option<&'static str> {
    build_env(option_env!("RENDEZVOUS_SERVER"))
}

pub fn rs_pub_key() -> &'static str {
    build_env(option_env!("RS_PUB_KEY")).unwrap_or(config::RS_PUB_KEY)
}

pub fn init() {
    {
        let mut name = config::APP_NAME.write().unwrap();
        if name.as_str() == "RustDesk" {
            *name = APP_NAME.to_owned();
        }
    }
    if let Some(server) = rendezvous_server() {
        let mut prod = config::PROD_RENDEZVOUS_SERVER.write().unwrap();
        if prod.is_empty() {
            *prod = server.to_owned();
        }
    }
}
