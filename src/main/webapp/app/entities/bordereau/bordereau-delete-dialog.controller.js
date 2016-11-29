(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('BordereauDeleteController',BordereauDeleteController);

    BordereauDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bordereau'];

    function BordereauDeleteController($uibModalInstance, entity, Bordereau) {
        var vm = this;

        vm.bordereau = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bordereau.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
