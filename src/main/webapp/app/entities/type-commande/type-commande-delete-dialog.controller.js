(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('TypeCommandeDeleteController',TypeCommandeDeleteController);

    TypeCommandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeCommande'];

    function TypeCommandeDeleteController($uibModalInstance, entity, TypeCommande) {
        var vm = this;

        vm.typeCommande = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeCommande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
