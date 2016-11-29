(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('CorpsEtatDeleteController',CorpsEtatDeleteController);

    CorpsEtatDeleteController.$inject = ['$uibModalInstance', 'entity', 'CorpsEtat'];

    function CorpsEtatDeleteController($uibModalInstance, entity, CorpsEtat) {
        var vm = this;

        vm.corpsEtat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CorpsEtat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
