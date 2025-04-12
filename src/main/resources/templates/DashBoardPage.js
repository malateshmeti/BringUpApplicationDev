

function logout() {
    // Redirect to login page
    window.location.href = "BringUpLoginPage.html";
}

function openModule(moduleName) {
    switch (moduleName) {
        case 'ElectricianMainModule':
            window.location.href = 'ElectricianMainModule.html';
            break;
        case 'InstallationMainModule':
            window.location.href = 'InstallationMainModule.html';
            break;
        case 'HomeApplianceMainModule':
            window.location.href = 'HomeApplianceMainModule.html';
            break;
        case 'PlumberMainModule':  
        case 'PainterMainModule':  
        case 'CarpenterMainModule':
        case 'LaundryMainModule':
        case 'CustomerSupportMainModule':
        case 'AboutUsMainModule':
            alert(moduleName + ' is under construction.');
            break;
        default:
            alert('Module not recognized.');
            break;
    }
}

