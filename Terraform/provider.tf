terraform {
  required_version = ">= 0.12.29"
  # required_providers {
  #   oci = ">= 3.90"
  #   tls = ">= 2.0"
  #   random = ">= 2.1"
  # }
}

provider "oci" {
  tenancy_ocid = var.tenancy_ocid
  region       = local.region_to_deploy

  user_ocid        = var.user_ocid
  fingerprint      = var.fingerprint
  private_key_path = var.private_key_path
}
