SUMMARY = "XCB: The X protocol C binding library"
DESCRIPTION = "The X protocol C-language Binding (XCB) is a replacement \
for Xlib featuring a small footprint, latency hiding, direct access to \
the protocol, improved threading support, and extensibility."
HOMEPAGE = "http://xcb.freedesktop.org"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=XCB"
SECTION = "x11/libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"

SRC_URI = "http://xcb.freedesktop.org/dist/libxcb-${PV}.tar.xz \
           file://0001-use-_Alignof-to-avoid-UB-in-ALIGNOF.patch"

SRC_URI[sha256sum] = "599ebf9996710fea71622e6e184f3a8ad5b43d0e5fa8c4e407123c88a59a6d55"

BBCLASSEXTEND = "native nativesdk"

DEPENDS = "xcb-proto xorgproto libxau libpthread-stubs libxdmcp"

PACKAGES_DYNAMIC = "^${PN}-.*"

FILES:${PN} = "${libdir}/libxcb.so.*"

inherit autotools pkgconfig features_check

# The libxau and others requires x11 in DISTRO_FEATURES
REQUIRED_DISTRO_FEATURES = "x11"

export PYTHON = "python3"

do_install:append () {
	chown root.root ${D}${datadir}/doc/${BPN}/tutorial -R
}

python populate_packages:prepend () {
    do_split_packages(d, '${libdir}', r'^libxcb-(.*)\.so\..*$', '${PN}-%s', 'XCB library module for %s', allow_links=True)
}
