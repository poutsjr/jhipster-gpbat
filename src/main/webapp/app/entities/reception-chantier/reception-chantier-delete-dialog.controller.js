(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ReceptionChantierDeleteController',ReceptionChantierDeleteController);

    ReceptionChantierDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReceptionChantier'];

    function ReceptionChantierDeleteController($uibModalInstance, entity, ReceptionChantier) {
        var vm = this;

        vm.receptionChantier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReceptionChantier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
