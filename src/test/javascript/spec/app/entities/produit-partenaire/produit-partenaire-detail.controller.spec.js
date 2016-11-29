'use strict';

describe('Controller Tests', function() {

    describe('ProduitPartenaire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProduitPartenaire, MockBordereau;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProduitPartenaire = jasmine.createSpy('MockProduitPartenaire');
            MockBordereau = jasmine.createSpy('MockBordereau');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProduitPartenaire': MockProduitPartenaire,
                'Bordereau': MockBordereau
            };
            createController = function() {
                $injector.get('$controller')("ProduitPartenaireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpbatApp:produitPartenaireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
