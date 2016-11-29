(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('DiagnosticChantierDeleteController',DiagnosticChantierDeleteController);

    DiagnosticChantierDeleteController.$inject = ['$uibModalInstance', 'entity', 'DiagnosticChantier'];

    function DiagnosticChantierDeleteController($uibModalInstance, entity, DiagnosticChantier) {
        var vm = this;

        vm.diagnosticChantier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DiagnosticChantier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
