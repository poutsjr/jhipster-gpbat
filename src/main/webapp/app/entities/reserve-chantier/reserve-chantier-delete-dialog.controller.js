(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ReserveChantierDeleteController',ReserveChantierDeleteController);

    ReserveChantierDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReserveChantier'];

    function ReserveChantierDeleteController($uibModalInstance, entity, ReserveChantier) {
        var vm = this;

        vm.reserveChantier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReserveChantier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
