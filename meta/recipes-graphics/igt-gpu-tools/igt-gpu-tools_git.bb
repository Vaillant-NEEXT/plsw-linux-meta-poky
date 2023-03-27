SUMMARY = "IGT GPU Tools"
DESCRIPTION = "IGT GPU Tools is a collection of tools for development and testing of the DRM drivers"
HOMEPAGE = "https://gitlab.freedesktop.org/drm/igt-gpu-tools"
BUGTRACKER = "https://gitlab.freedesktop.org/drm/igt-gpu-tools/-/issues"

LIC_FILES_CHKSUM = "file://COPYING;md5=67bfee4df38fa6ecbe3a675c552d4c08"

LICENSE = "MIT"

inherit meson pkgconfig

SRCREV = "2b29e8ac07fbcfadc48b9d60e4d736a6e3b289ab"
PV = "1.27.1"

SRC_URI = "git://gitlab.freedesktop.org/drm/igt-gpu-tools.git;protocol=https;branch=master \
           file://0001-Support-procps-4.x.patch \
           "

S = "${WORKDIR}/git"

DEPENDS += "libdrm libpciaccess cairo udev glib-2.0 procps libunwind kmod openssl elfutils alsa-lib json-c bison-native"
RDEPENDS:${PN} += "bash perl"
RDEPENDS:${PN}-tests += "bash"

PACKAGE_BEFORE_PN = "${PN}-benchmarks ${PN}-tests"

PACKAGECONFIG[chamelium] = "-Dchamelium=enabled,-Dchamelium=disabled,gsl xmlrpc-c"

EXTRA_OEMESON = "-Ddocs=disabled -Drunner=enabled -Dsrcdir=/usr/src/debug/${PN}/${PV}-${PR}/git/"
COMPATIBLE_HOST = "(x86_64.*|i.86.*|arm.*|aarch64).*-linux"
COMPATIBLE_HOST:libc-musl:class-target = "null"
SECURITY_LDFLAGS = "${SECURITY_X_LDFLAGS}"

gputools_sysroot_preprocess() {
	rm -f ${SYSROOT_DESTDIR}${libdir}/pkgconfig/intel-gen4asm.pc
}
SYSROOT_PREPROCESS_FUNCS += "gputools_sysroot_preprocess"

do_install:append() {
    install -d ${D}/usr/share/${BPN}/scripts
    install ${S}/scripts/run-tests.sh ${D}/usr/share/${BPN}/scripts
    install -d ${D}/usr/share/${BPN}/runner
    install -D ${B}/runner/igt_runner ${D}/usr/share/${BPN}/runner
    install -D ${B}/runner/igt_resume ${D}/usr/share/${BPN}/runner
}

FILES:${PN}-benchmarks += "${libexecdir}/${BPN}/benchmarks"
FILES:${PN}-tests += "\
        ${libexecdir}/${BPN}/*\
        ${datadir}/${BPN}/1080p-right.png\
        ${datadir}/${BPN}/1080p-left.png\
        ${datadir}/${BPN}/pass.png\
        ${datadir}/${BPN}/test-list.txt"