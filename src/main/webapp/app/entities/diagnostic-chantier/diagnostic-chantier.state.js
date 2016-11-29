(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('diagnostic-chantier', {
            parent: 'entity',
            url: '/diagnostic-chantier?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.diagnosticChantier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diagnostic-chantier/diagnostic-chantiers.html',
                    controller: 'DiagnosticChantierController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('diagnosticChantier');
                    $translatePartialLoader.addPart('typeDiagnostic');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('diagnostic-chantier-detail', {
            parent: 'entity',
            url: '/diagnostic-chantier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.diagnosticChantier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diagnostic-chantier/diagnostic-chantier-detail.html',
                    controller: 'DiagnosticChantierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('diagnosticChantier');
                    $translatePartialLoader.addPart('typeDiagnostic');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DiagnosticChantier', function($stateParams, DiagnosticChantier) {
                    return DiagnosticChantier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'diagnostic-chantier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('diagnostic-chantier-detail.edit', {
            parent: 'diagnostic-chantier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostic-chantier/diagnostic-chantier-dialog.html',
                    controller: 'DiagnosticChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiagnosticChantier', function(DiagnosticChantier) {
                            return DiagnosticChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diagnostic-chantier.new', {
            parent: 'diagnostic-chantier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostic-chantier/diagnostic-chantier-dialog.html',
                    controller: 'DiagnosticChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                typeDiagnostic: null,
                                dateDiagnostic: null,
                                referenceDiagnostic: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('diagnostic-chantier', null, { reload: 'diagnostic-chantier' });
                }, function() {
                    $state.go('diagnostic-chantier');
                });
            }]
        })
        .state('diagnostic-chantier.edit', {
            parent: 'diagnostic-chantier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostic-chantier/diagnostic-chantier-dialog.html',
                    controller: 'DiagnosticChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiagnosticChantier', function(DiagnosticChantier) {
                            return DiagnosticChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diagnostic-chantier', null, { reload: 'diagnostic-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diagnostic-chantier.delete', {
            parent: 'diagnostic-chantier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostic-chantier/diagnostic-chantier-delete-dialog.html',
                    controller: 'DiagnosticChantierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DiagnosticChantier', function(DiagnosticChantier) {
                            return DiagnosticChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diagnostic-chantier', null, { reload: 'diagnostic-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
