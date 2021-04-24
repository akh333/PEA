resource "oci_core_vcn" "ApplicationVCN" {
  cidr_block     = var.VCN-CIDR
  compartment_id = oci_identity_compartment.ApplicationCompartment.id
  display_name   = "ApplicationVCN"
}

resource "oci_core_internet_gateway" "ApplicationInternetGateway" {
  compartment_id = oci_identity_compartment.ApplicationCompartment.id
  display_name   = "ApplicationInternetGateway"
  vcn_id         = oci_core_vcn.ApplicationVCN.id
}

resource "oci_core_route_table" "ApplicationRouteTableViaIGW" {
  compartment_id = oci_identity_compartment.ApplicationCompartment.id
  vcn_id         = oci_core_vcn.ApplicationVCN.id
  display_name   = "ApplicationRouteTableViaIGW"

  route_rules {
    destination       = "0.0.0.0/0"
    destination_type  = "CIDR_BLOCK"
    network_entity_id =  oci_core_internet_gateway.ApplicationInternetGateway.id
  }
}

resource "oci_core_security_list" "ApplicationOKESecurityList" {
    compartment_id = oci_identity_compartment.ApplicationCompartment.id
    display_name = "ApplicationOKESecurityList"
    vcn_id = oci_core_vcn.ApplicationVCN.id
    
    egress_security_rules {
        protocol = "All"
        destination = "0.0.0.0/0"
    }

    /* This entry is necesary for DNS resolving (open UDP traffic). */
    ingress_security_rules {
        protocol = "17"
        source = var.VCN-CIDR
    }
}

resource "oci_core_subnet" "ApplicationClusterSubnet" {
  cidr_block          = var.ApplicationClusterSubnet-CIDR
  compartment_id      = oci_identity_compartment.ApplicationCompartment.id
  vcn_id              = oci_core_vcn.ApplicationVCN.id
  display_name        = "ApplicationClusterSubnet"

  security_list_ids = [oci_core_vcn.ApplicationVCN.default_security_list_id, oci_core_security_list.ApplicationOKESecurityList.id]
  route_table_id    = oci_core_route_table.ApplicationRouteTableViaIGW.id
}

resource "oci_core_subnet" "ApplicationNodePoolSubnet" {
  cidr_block          = var.ApplicationNodePoolSubnet-CIDR
  compartment_id      = oci_identity_compartment.ApplicationCompartment.id
  vcn_id              = oci_core_vcn.ApplicationVCN.id
  display_name        = "ApplicationNodePoolSubnet"

  security_list_ids = [oci_core_vcn.ApplicationVCN.default_security_list_id, oci_core_security_list.ApplicationOKESecurityList.id]
  route_table_id    = oci_core_route_table.ApplicationRouteTableViaIGW.id
}