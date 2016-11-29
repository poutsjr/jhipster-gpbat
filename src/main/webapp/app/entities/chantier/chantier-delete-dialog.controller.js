(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ChantierDeleteController',ChantierDeleteController);

    ChantierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Chantier'];

    function ChantierDeleteController($uibModalInstance, entity, Chantier) {
        var vm = this;

        vm.chantier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Chantier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
